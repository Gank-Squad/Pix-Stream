import React from 'react';

import { API_ENDPOINTS, API_TEMPLATES, ROUTES } from '../../constants';
import { formatStringB, addQueryParams } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';
import VideoPlayer from '../elements/Video';
import VideoContainer from '../elements/VideoContainer';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;
    const _tags_full = params.get('tags') || "";
    const tags_full = _tags_full.split(",").map(Number).filter((value) => !isNaN(value));;

    if(tags_full.length === 0)
    {
        window.location.href = ROUTES.home;
    }

    const [search, setSearch] = React.useState([]);
    const [mediaData, setMediaData] = React.useState([]);


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    React.useEffect(() => 
    {
        console.log(`MediaData ${JSON.stringify(mediaData)} was loaded!`);
    }, [mediaData]);
    
    React.useEffect(() => 
    {
        console.log("Updating media with new search " + JSON.stringify(search));
        loadMedia();
    }, []); // search


    function loadMedia()
    {
        const ids = tags_full.map(element => ({ "tag_id" : element}));
        
        console.log(JSON.stringify(ids));
        const url = addQueryParams(API_TEMPLATES.get_posts_with_tags.url, {
            tags : true
        });
        
        const fetchData = {
            method : "POST",
            headers : { "Content-Type": "application/json" },
            body : JSON.stringify(ids)
        }

        console.log("loading media from api " + url + " " + JSON.stringify(fetchData));

        fetch(url, fetchData).then(resp => {
            if (resp.status === 200)
            {
                console.log("Got some sick json");
                return resp.json();    
            }
            else
            {
                console.log("Status: " + resp.status);
                return Promise.reject("server");
            }
        }).then(dataJson => {
            console.log(dataJson);
            setMediaData(dataJson);
        }).catch(err => {
            if (err === "server") return
            console.log(err)
        })
    }


    function searchButtonPressed()
    {
        const url = formatStringB('/results?tags={IDS}', 
            search.map(elements => elements.tag_id).join(","))
        
        window.location.href = url;
        
    }

    function searchCallback(searchItems)
    {
        setSearch(searchItems);
    }

    function redirect_tags()
    {
        window.location.href = '/tags';
    }
    function redirect_upload()
    {
        window.location.href = '/upload';
    }
    function redirect_home()
    {
        window.location.href = '/home';
    }

    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : false,
        "searchButtonPressed" : searchButtonPressed,
        "selectedTagIds" : tags_full
    }

    const sidebar = React.useRef("");
    const hamburger = React.useRef("");
    const main = React.useRef("");

    function redirect_media(hash)
    {
        if (hash === null || hash === "")
        {
            console.log("hash is empty, doing nothing")
            return;
        }
        console.log("hash " + hash);
        window.location.href = '/media?hash=' + hash;
    }

    function toggleSidebarVisibility()
    {
        // ik this is really bad code, and now there isn't a css animation, but I couldn't get it to work
        if (sidebar.current.getAttribute("data-te-sidenav-hidden") == "false")
        {
            sidebar.current.setAttribute("data-te-sidenav-hidden", "true");
            hamburger.current.setAttribute("class", "bg-blue-700 hover:bg-blue-500 text-white font-bold py-2 px-4 border border-blue-700 rounded-l");
            main.current.setAttribute("class", "overflow-y-scroll py-12 px-12")
        }
        else
        {
            sidebar.current.setAttribute("data-te-sidenav-hidden", "false");
            hamburger.current.setAttribute("class", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l");
            main.current.setAttribute("class", "overflow-y-scroll py-12 px-72")
        }
    }

    return (

        <div className="flex flex-col h-screen overflow-auto">

            {/* This is the sidebar/tag search */}
            <nav ref={sidebar}
                className="group fixed top-20 left-0 h-screen w-60 -translate-x-60 overflow-y-auto overflow-x-hidden shadow-[0_4px_12px_0_rgba(0,0,0,0.07),_0_2px_4px_rgba(0,0,0,0.05)] data-[te-sidenav-hidden='false']:translate-x-0 bg-custom-dark-blue"
                data-te-sidenav-init
                data-te-sidenav-hidden="false"
            >
                <ul className="relative m-0 list-none px-[0.2rem]" data-te-sidenav-menu-ref>
                    <TagSidebar {...tagSidebarProps} />
                </ul>
            </nav>


            <header className="w-full p-4 h-20 bg-custom-dark-blue text-left absolute">
                <div>
                    {/* The following is the code for the hamburger menu
                    but I'm not really sure how to make it show up inline with everything else */}
                    <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l"
                        data-te-sidenav-toggle-ref
                        aria-haspopup="true"
                        onClick={toggleSidebarVisibility}
                        ref={hamburger}>
                        <span className="block [&>svg]:h-5 [&>svg]:w-5 [&>svg]:text-white">
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="currentColor"
                                className="h-5 w-5">
                                    <path
                                    fillRule="evenodd"
                                    d="M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z"
                                    clipRule="evenodd" />
                            </svg>
                        </span>
                    </button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l" onClick={redirect_home}>Home</button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" onClick={redirect_tags}>Tags</button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" onClick={redirect_upload}>Upload</button>
                </div>
            </header>

            <main className="flex-1 overflow-y-auto px-80 py-20" ref={main}>
            {/* PUT ALL DISPLAY STUFF IN HERE, ANYTHING OUTSIDE MAY NOT BE FORMATED CORRECTLY */}

                <p className="text-xl font-bold text-custom-white">Displaying search results:</p>


                {/* 
                
[
    {
        "post_id": 1,
        "title": "Shondo looking cute today!!!",
        "description": "Just look at my Imouto Wifee!!! <3 <#",
        "created_at": 1681030933270,
        "files": [
            {
                "tags": null,
                "hash_id": 1,
                "mime": "image/jpg",
                "mime_int": 1,
                "file_size": 143885,
                "width": 850,
                "height": 1275,
                "duration": 0,
                "has_audio": false,
                "hash": "962b5042569c658beb15b16b257a290847e9ee71d3ecfa4ccf732512c16f7348"
            }
        ]
    }
]
                */}


                {mediaData.map((json, index) => 
                {
                    console.log(json);
                    const props = {
                        "image" : formatStringB(API_TEMPLATES.get_thumbnail.url, json.files[0].hash),
                        "caption": `PostID  ${json.post_id}    ${json.title}           Created On ${json.created_at}       Description ${json.description}`,
                        "style" : {
                            display: 'inline-block',
                            width: '200px',
                            'margin': '20px',
                            border: '1px solid white',
                            "verticalAlign" : "bottom",
                        }
                    }
                    
                    function redirect_media(hash)
                    {
                        if (hash === null || hash === "")
                        {
                            console.log("hash is empty, doing nothing")
                            return "";
                        }
                        console.log("post " + hash);
                        return '/media?post=' + hash;
                    }

                    return <a key={index}  href={redirect_media(json.post_id)}><ImageContainer {...props} imgError={e => console.log("image errr")}></ImageContainer></a>;
                })}
            </main>
        </div>
    )
}