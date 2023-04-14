import React from 'react';

import { API_ENDPOINTS, API_TEMPLATES } from '../../constants';
import TagSidebar from '../elements/TagSidebar';
import HeaderBar from '../elements/HeaderBar';
import { formatStringB } from '../../requests';

export default function Default(props)
{
    // define vars
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [tags, setTags] = React.useState([]);
    const [search, setSearch] = React.useState([]);
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    const tagNamespace = React.useRef();
    const tagSubtag = React.useRef();


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    // on page load
    React.useEffect(()=>{
        loadTags();
    }, [])


    async function loadTags()
    {
        // fetch tags and set tag data
        await fetch(
            API_ENDPOINTS.media.get_tags, {method:"GET"}
        ).then(resp =>{
            if (resp.status ===200)
            {
                return resp.json();
            }
            console.log("Status: " + resp.status);
            return Promise.reject("server");
        }).then(dataJson =>{
            setTags(dataJson);
        }).catch(err =>{
            if (err === "server") return;
            console.log(err);
        })
    }


    function searchCallback(searchItems)
    {
        setSearch(searchItems);
    }

    function searchButtonPressed()
    {
        const url = formatStringB('/results?tags={IDS}', 
        search.map(elements => elements.tag_id).join(","))
    
        console.log(url);
        window.location.href = url;
    }

    // create props for the sidebar
    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : false,
        "searchButtonPressed" : searchButtonPressed,
    }

    async function createTag(e)
    {
        // check if valid subtag
        if (tagSubtag === null || tagSubtag.current.value === "")
        {
            alert("you must enter a subtag");
            return;
        }

        // since its valid, make request to api
        e.preventDefault();

        const data = [{ 
            "namespace" : tagNamespace.current.value.toLowerCase(),
            "subtag" : tagSubtag.current.value.toLowerCase(),
        }]

        // post req to api
        await fetch(API_TEMPLATES.create_tag.url, 
            {
                method : "POST",
                headers : { "Content-Type": "application/json" },
                body : JSON.stringify(data)
            })
        .then(resp => {
            if (resp.status === 200)
            {
                console.log("200 status, tag uploaded")
                return resp.json();    
            }
            else
            {
                console.log("Status: " + resp.status);
                return Promise.reject("server");
            }
        })

        // refresh the page to update tags instead of doing
        // in a not terrible way
        window.location.href = "/tags"
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

                        {/* Create Tag text boxes, button and title, set up to resize dynamically */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-x-6 gap-y-8 bg-button-depressed m-24 px-8 pt-2 pb-4 rounded-3xl">
                            <p className="col-span-full text-center text-4xl font-bold font-sans">Create Tag</p>
                            <input className="text-2xl" ref={tagNamespace} id="tag-search" type="text" placeholder="Tag Namespace (e.g. body)"/>
                            <input className="text-2xl" ref={tagSubtag} id="tag-search" type="text" placeholder="Subtag (e.g. hands)"/>
                            
                            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l text-2xl" onClick={createTag}>Create Tag</button>
                        </div>
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-x-6 gap-y-8 bg-button-depressed m-24 px-8 pt-2 pb-4 rounded-3xl">
                            <p className="col-span-full text-center text-4xl font-bold font-sans">Tags</p>
                        {
                            tags.map((json, index) =>{
                                // if there is no namespace, don't add the :
                                const tag = json.namespace === "" ? json.subtag : json.namespace + ":" + json.subtag;

                                // make each tag a link to the search results page for that tag
                                return <a href={"/results?tags=" + json.tag_id}>
                                        <p className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-1 px-1 rounded">{tag}</p>
                                    </a>
                            })
                        }
                        </div>
                    </main>
                </div>
        </div>
    )
}