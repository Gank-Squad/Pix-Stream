import React from 'react';

import { API_ENDPOINTS, DISPLAY_TYPES, MAX_DIMENSIONS } from '../../constants';
import { formatStringB, addQueryParams } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import MediaContainer from '../elements/MediaContainer';
import HeaderBar from '../elements/HeaderBar';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [search, setSearch] = React.useState([]);
    const [mediaData, setMediaData] = React.useState([]);
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    // on page load, load media from api
    React.useEffect(() => {
        loadMedia();
    }, []);

    React.useEffect(() => 
    {
        console.log("Updating media with new search " + JSON.stringify(search));
    }, [search]);


    // fetch media from api, set media data appropriately
    function loadMedia()
    {
        // arbitrarily setting the limit to 10, can be anything
        let url = addQueryParams(API_ENDPOINTS.media.get_posts, {limit : 10});

        const fetchData = {
            method : "GET"
        }

        fetch(url, fetchData).then(resp => {
            if (resp.status === 200)
            {
                return resp.json();    
            }
            else
            {
                console.log("Status: " + resp.status);
                return Promise.reject("server");
            }
            // setting media data
        }).then(dataJson => {
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
  <HeaderBar toggleSidebarVisibility={() => setSidebarVisible(!sidebarVisible)} />
  
  <div className="flex flex-row flex-1 overflow-y-auto">
    {sidebarVisible && (
      <nav
        className="flex-none group h-full w-60 -translate-x-60 overflow-y-auto overflow-x-hidden shadow-[0_4px_12px_0_rgba(0,0,0,0.07),_0_2px_4px_rgba(0,0,0,0.05)] data-[te-sidenav-hidden='false']:translate-x-0 bg-custom-dark-blue"
        data-te-sidenav-init
        data-te-sidenav-hidden="false"
      >
        <ul className="relative m-0 h-full list-none px-[0.2rem]" data-te-sidenav-menu-ref>
          <TagSidebar {...tagSidebarProps} />
        </ul>
      </nav>
    )}

    <main className="flex-1 flex-wrap overflow-y-auto">
                {/* create preview for all the images loaded from api */}
                { mediaData.map((json, index) => {

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
                        // function for getting the url for each post
                        function redirect_media(postId)
                        {
                            if (postId === null || postId < 1)
                            {
                                console.log("invalid postId, doing nothing")
                                return "";
                            }
                            return '/media?post=' + postId;
                        }
                        // html to render the stuff, make them all links to redirect to the media page as well
                        return <a key={index} href={redirect_media(json.post_id)} 
                        className='border p-4 space-x-4 inline-block '>
                            <MediaContainer {...props} />
                            {/* add the title here, needs to be changed in the future */}
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
