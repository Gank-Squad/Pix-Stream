import React from 'react';

import { API_TEMPLATES, ROUTES, DISPLAY_TYPES } from '../../constants';
import { formatStringB } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';
import MediaContainer from '../elements/MediaContainer';
import VideoPlayer from '../elements/Video';
import HeaderBar from '../elements/HeaderBar';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;
    const post = params.get("post") || 0;

    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    if(post <= 0)
    {
        window.location.href = ROUTES.home;
    }

    const [search, setSearch] = React.useState([]);
    const [mediaData, setMediaData] = React.useState({});

    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    React.useEffect(() => 
    {
        // console.log(mediaData);
    }, [mediaData]);

    React.useEffect(() => 
    {

        setSidebarVisible(!sidebarVisible)

        if (post === null || post <= 0)
        {
            alert("invalid url - no media loaded");
            return;
        }
        
        const url = formatStringB(API_TEMPLATES.get_post.url, post);

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

    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : false,
        "searchButtonPressed" : searchButtonPressed,
    }

    function getMediaDisplayContainer(mediaJson)
    {
        if(!mediaJson || !mediaJson.files)
            return;

        const props = {
            "hash" : mediaData.files[0].hash,
            "displayType" : DISPLAY_TYPES.general_display,
            metaData : {
                id : mediaData.post_id.id,
                title : mediaData.title,
                description : mediaData.description,
                created_at : mediaData.created_at,
                mime_int : mediaData.files[0].mime_int,
                duration : mediaData.files[0].duration,
                width : mediaData.files[0].width,
                height : mediaData.files[0].height,
            }
        };

        return (
            <div>
                <MediaContainer {...props} />
            </div>
        )
    }

    if(mediaData == null)
    {
        return <div>Loading...</div>
    }
    return (
<div className="flex flex-col h-screen">
        <HeaderBar toggleSidebarVisibility={()=>setSidebarVisible(!sidebarVisible)} />
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

                
            <table className="text-custom-white">
                    <tbody>
                        <tr><td><p>{mediaData.title}</p></td></tr>
                        <tr><td>{mediaData.description}</td></tr>
                    </tbody>
                </table>
            

                {getMediaDisplayContainer(mediaData)}
                
            </main>
        </div>
</div>

    )
}