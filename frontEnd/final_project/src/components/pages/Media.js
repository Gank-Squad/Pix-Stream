import React from 'react';

import { API_ENDPOINTS, API_TEMPLATES, ROUTES } from '../../constants';
import { formatStringB, addQueryParams } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';
import VideoPlayer from '../elements/Video';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;
    const post = params.get("post") || 0;

    const sidebar = React.useRef("");
    const hamburger = React.useRef("");
    const main = React.useRef("");

    if(post <= 0)
    {
        window.location.href = ROUTES.home;
    }

    const [search, setSearch] = React.useState([]);
    const [mediaData, setMediaData] = React.useState({});

    const vprops = {
        m3u8: API_ENDPOINTS.media.get_file + "D2765EC844F9C92DF35152A5725E0ED381221F202B9BDC190DF599942DEFE930",
        domain: API_ENDPOINTS.media.get_file + "D2765EC844F9C92DF35152A5725E0ED381221F202B9BDC190DF599942DEFE930/"
    };

    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    React.useEffect(() => 
    {
        console.log(mediaData);
    }, [mediaData]);

    React.useEffect(() => 
    {
        if (post === null || post <= 0)
        {
            alert("invalid url - no media loaded");
            return;
        }
        
        const url = formatStringB(API_TEMPLATES.get_post.url, post);
        console.log(url);
        fetch(url, {method:"GET"}).then(resp =>
            {
                if (resp.status === 200)
                {
                    console.log("loading media page for postId: " + post);
                    let temp = resp.json();
                    console.count("RESPONSE:" + temp)
                    return temp;
                }
                else
                {
                    console.log("Status: " + resp.status);
                    return;
                }
            }
        ).then(dataJson => {
            setMediaData(dataJson);
        }).catch(err => {
            if (err === "server") return
            console.log(err)
        })
    }, []);


    function searchButtonPressed()
    {
        let url = '/results?tags=';
        const ids = search.map(elements => ({tag_id : elements.tag_id}));

        ids.forEach(id => (url += id.tag_id + ","));

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

    function getMediaDisplayContainer(mediaJson)
    {
        if(!mediaJson || !mediaJson.files)
            return;

        const props = {
            "image" : formatStringB(API_TEMPLATES.get_file.url, mediaJson.files[0].hash),
            "hlsUrl": formatStringB(API_TEMPLATES.get_file.url, mediaJson.files[0].hash),
            "caption": mediaJson.title,
            "style" : {
                display: 'inline-block',
                margin: '20px',
                border: '1px solid white',
                verticalAlign : "bottom",
            }
        }
                            
        if(mediaJson.files[0].mime_int >= 20)
        {
            return <VideoPlayer{...props}></VideoPlayer>;
        }
        
        return <ImageContainer {...props} imgError={e => console.log("image errr")}></ImageContainer>;
    }

    if(mediaData == null)
    {
        return <div>Loading...</div>
    }
    return (
        <div className="flex flex-col h-screen overflow-auto">

            {/* This is the sidebar/tag search */}
            <nav ref={sidebar}
                className="group fixed top-20 left-0 h-screen w-60 -translate-x-60 overflow-y-auto overflow-x-hidden shadow-[0_4px_12px_0_rgba(0,0,0,0.07),_0_2px_4px_rgba(0,0,0,0.05)] data-[te-sidenav-hidden='false']:translate-x-0 bg-custom-dark-blue"
                data-te-sidenav-init
                data-te-sidenav-hidden="true"
            >
                <ul className="relative m-0 list-none px-[0.2rem]" data-te-sidenav-menu-ref>
                    <TagSidebar {...tagSidebarProps} />
                </ul>
            </nav>

            <header className="w-full p-4 h-20 bg-custom-dark-blue absolute">
                <table>
                    <tbody>
                    <tr>
                <td><div className="text-left inline-block">
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
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" disabled onClick={redirect_tags}>Tags</button>
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" onClick={redirect_upload}>Upload</button>
                </div></td>

                <td className="center"><div className="text-center inline"><p className="text-xl font-bold">PixStream</p></div></td>
                </tr></tbody>
                </table>
            </header>

            <main className="overflow-y-auto py-12 px-72" ref={main}>
            {/* PUT ALL DISPLAY STUFF IN HERE, ANYTHING OUTSIDE MAY NOT BE FORMATED CORRECTLY */}
            

            {/* {
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
    } */}

<table>
    <tbody>
        <tr><td><p>{mediaData.title}</p></td></tr>
        <tr><td>{mediaData.description}</td></tr>
    </tbody>
</table>
    

        {getMediaDisplayContainer(mediaData)}
  
            </main>


        </div>









    )
}