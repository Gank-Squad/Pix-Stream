import React from 'react';

import { API_ENDPOINTS, API_TEMPLATES } from '../../constants';
import TagSidebar from '../elements/TagSidebar';
import HeaderBar from '../elements/HeaderBar';
import postData from '../../requests';

export default function Default(props)
{
    const { cookies } = props;

    const API = API_ENDPOINTS.search.get_files_with_tags;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [images, setImageData] = React.useState([]);
    const [tags, setTags] = React.useState([]);
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    const tagNamespace = React.useState("");
    const tagSubtag = React.useState("");


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    React.useEffect(()=>{
        loadTags();
    }, [])


    async function loadTags()
    {
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
        if(searchItems.length === 0)
        {
            setImageData([]);
            return;
        }

        let ids = []
        searchItems.forEach(element => 
        {
            ids.push({
                tag_id: element.tag_id
            })
        });

        console.log("sending " + JSON.stringify(ids));

        fetch(API, {
            method: "post",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ids)
        })
            .then(resp => {
                if (resp.status === 200) {
                    return resp.json()
                } else {
                    console.log("Status: " + resp.status)
                    return Promise.reject("server")
                }
            })
            .then(dataJson => {

                setImageData(dataJson);
            })
            .catch(err => {
                if (err === "server") return
                console.log(err)
            })
    }

    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : false,
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
                                const tag = json.namespace + ":" + json.subtag;

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