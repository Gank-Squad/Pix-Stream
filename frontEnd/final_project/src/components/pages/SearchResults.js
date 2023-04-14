import React from 'react';

import { MAX_DIMENSIONS, API_TEMPLATES, ROUTES, DISPLAY_TYPES } from '../../constants';
import { formatStringB, addQueryParams } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import HeaderBar from '../elements/HeaderBar';

import MediaContainer from '../elements/MediaContainer';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;
    const _tags_full = params.get('tags') || "";
    const tags_full = _tags_full.split(",").map(Number).filter((value) => !isNaN(value));;
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    // if there are no tags in the search, redirect to home
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

    // log changes to mediaData
    React.useEffect(() => 
    {
        console.log(`MediaData ${JSON.stringify(mediaData)} was loaded!`);
    }, [mediaData]);
    
    // on page load, load media from api
    React.useEffect(() => 
    {
        console.log("Updating media with new search " + JSON.stringify(search));
        loadMedia();
    }, []); // search

    // load media from api
    function loadMedia()
    {
        // make json with tag ids
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

        // fetch posts from api with tags from param
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
            // set media data
            console.log(dataJson);
            setMediaData(dataJson);
        }).catch(err => {
            if (err === "server") return
            console.log(err)
        })
    }


    // redirect to search results page with correct tags
    function searchButtonPressed()
    {
        const url = formatStringB('/results?tags={IDS}', 
        search.map(elements => elements.tag_id).join(","))

        console.log(url);
        window.location.href = url;
    }

    function searchCallback(searchItems)
    {
        setSearch(searchItems);
    }

    // props for sidebar
    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : false,
        "searchButtonPressed" : searchButtonPressed,
    }

    return (
        <div className="flex flex-col h-screen">
            {/* create header bar */}
                <HeaderBar toggleSidebarVisibility={()=>setSidebarVisible(!sidebarVisible)} />
                {/* create sidebar */}
                <div className="flex flex-row flex-1">

                    {sidebarVisible &&
                    <nav
                        className="group  top-0 left-0 h-screen w-60 -translate-x-60 overflow-y-auto overflow-x-hidden shadow-[0_4px_12px_0_rgba(0,0,0,0.07),_0_2px_4px_rgba(0,0,0,0.05)] data-[te-sidenav-hidden='false']:translate-x-0 bg-custom-dark-blue"
                        data-te-sidenav-init
                        data-te-sidenav-hidden="false"
                        >
                        <ul className="relative m-0 list-none px-[0.2rem]" data-te-sidenav-menu-ref>
                            <TagSidebar {...tagSidebarProps} />
                        </ul>
                    </nav>}


            <main className="flex-1 flex-wrap">
                        {/* display all the media in tiles using MediaContainer */}
                <p className="text-xl font-bold text-custom-white">Displaying search results:</p>

                        { mediaData.map((json, index) => {

                            // create props
                        const props = {
                            "hash" : json.files[0].hash,
                            "displayType" : DISPLAY_TYPES.thumb_preview,
                            metaData : {
                                id : json.post_id,
                                title : json.title,
                                description : json.description,
                                created_at : json.created_at,
                                mime_int : json.files[0].mime_int,
                                duration : json.files[0].duration,
                                width : json.files[0].width,
                                height : json.files[0].height,
                            }
                        }
                        // function to generate urls
                        function redirect_media(postId)
                        {
                            if (postId === null || postId < 1)
                            {
                                console.log("invalid postId, doing nothing")
                                return "";
                            }
                            console.log("hash " + postId);
                            return '/media?post=' + postId;
                        }

                        // create preview and make it a link to the post
                        return <a key={index} href={redirect_media(json.post_id)} 
                        className='border p-4 space-x-4 inline-block '>
                            <MediaContainer {...props} />
                            
                            <div className={`w-[${MAX_DIMENSIONS.preview_width}px]`}>
                                <p className='text-ellipsis truncate'>{json.title}</p>
                            </div>
                        </a>;
                        })
                        }
                    </main>
                </div>
        </div>
    )
}