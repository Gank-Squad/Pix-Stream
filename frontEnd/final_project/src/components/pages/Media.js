import React from 'react';

import { API_TEMPLATES, ROUTES, DISPLAY_TYPES } from '../../constants';
import { formatStringB } from '../../requests';
import TagSidebar from '../elements/TagSidebar';
import MediaContainer from '../elements/MediaContainer';
import HeaderBar from '../elements/HeaderBar';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;
    const post = params.get("post") || 0;

    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    // if there is no post, redirect to home page isntead of 404ing or displaying empty media page
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

    // any time there is a change to mediaData log it
    React.useEffect(() => 
    {
        console.log(mediaData);
    }, [mediaData]);


    // on page load, fetch data from api and set media data
    React.useEffect(() => 
    {
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
            // set media data
            setMediaData(dataJson);
        }).catch(err => {
            if (err === "server") 
            {
                return
            }
            console.log(err)
        })
    }, []);

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
        "displayOnlyMode" : true,
        "displayTags" : (mediaData && mediaData.files && mediaData.files[0].tags)
        ? mediaData.files[0].tags : []
    }

    function getMediaDisplayContainer(mediaJson)
    {
        if(!mediaJson || !mediaJson.files)
        {
            return;
        }

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
            <div className='m-4'>
                <MediaContainer {...props} />
            </div>
        )
    }

    if(mediaData == null)
    {
        return <div>Loading...</div>
    }
    function humanFileSize(bytes, si=false, dp=1) {
        const thresh = si ? 1000 : 1024;
      
        if (Math.abs(bytes) < thresh) {
          return bytes + ' B';
        }
      
        const units = si 
          ? ['kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'] 
          : ['KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB', 'ZiB', 'YiB'];
        let u = -1;
        const r = 10**dp;
      
        do {
          bytes /= thresh;
          ++u;
        } while (Math.round(Math.abs(bytes) * r) / r >= thresh && u < units.length - 1);
      
      
        return bytes.toFixed(dp) + ' ' + units[u];
      }
      
    function msToTime(s) 
    {
        if(s <= 0)
        return "N/A";
        var ms = s % 1000;
        s = (s - ms) / 1000;
        var secs = s % 60;
        s = (s - secs) / 60;
        var mins = s % 60;
        var hrs = (s - mins) / 60;
      
        hrs = hrs.toString()
        mins = mins.toString()
        secs = secs.toString()

        if(hrs === "0" && mins === "0")
            return secs + " seconds";

        if(hrs === "0")
            return mins + " minutes, " + secs + " seconds";

        if(hrs.length < 2)
            hrs = "0" + hrs;

        if(mins.length < 2)
            mins = "0" + mins;

        if(secs.length < 2)
            secs = "0" + secs;

        
        return hrs + ':' + mins + ':' + secs;
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
      <center>
        {getMediaDisplayContainer(mediaData)}

        <div className="mx-8  grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-8 bg-button-depressed px-8 pt-2 pb-4 rounded-3xl">
          <table className="text-custom-white text-ellipsis wrap">
            <tbody>
              <p className="text-xl font-bold text-custom-white">{mediaData.title}</p>
                {mediaData.description&&mediaData.description.split(/\n/g).map(x => <tr><tr><span>{x}</span></tr></tr>)}
              <br />
              {mediaData &&
                mediaData.files &&
                mediaData.files.map((file, index) => {
                  return (
                    <div>
                      <tr>
                        <td>
                          <span>SHA256: {file.hash}</span>
                        </td>
                      </tr>
                      <tr>
                        <td>Size: {humanFileSize(file.file_size)}</td>
                      </tr>
                      <tr>
                        <td>Duration: {msToTime(file.duration)}</td>
                      </tr>
                      <tr>
                        <td>Width: {file.width}</td>
                      </tr>
                      <tr>
                        <td>Height: {file.height}</td>
                      </tr>
                    </div>
                  );
                })}
            </tbody>
          </table>
        </div>
      </center>
    </main>
  </div>
</div>
    )
}