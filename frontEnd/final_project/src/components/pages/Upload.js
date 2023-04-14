import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import { API_ENDPOINTS, API_TEMPLATES, DISPLAY_TYPES } from '../../constants';

import { formatStringB } from '../../requests';
import HeaderBar from '../elements/HeaderBar';
import MediaContainer from '../elements/MediaContainer';

import postData from '../../requests';

export default function Default(props)
{
    const { cookies } = props;

    // create vars
    const API = API_ENDPOINTS.search.get_files_with_tags;

    const [searchItems , setSearchItems] = React.useState([]);
    const [files       , setFiles  ] = React.useState([]);
    const [filePreviews, setPreview] = React.useState([]);
    const [displayProgress  , setProgress] = React.useState("");
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    const mediaFile = React.useRef("");
    const title = React.useRef("");
    const description = React.useRef("");

    // number of images to display, limiting to 1 since we don't support more atm
    const previewLoadCount = 1;

    // I don't think this is necessary, and making unneeded calls to api
    function searchCallback(searchItems)
    {
        setSearchItems(searchItems);
    }

    const tagSidebarProps = {
        "searchCallback" : searchCallback,
        "hideSearchButton" : true,
        "createTagButton" : true,
    }

    function generatePreview()
    {
        // if there is no file uploaded, do nothing
        if (!mediaFile.current.files || mediaFile.current.files.length === 0)
        {
            return;
        }

        const _files = Array.from(mediaFile.current.files);

        let newFiles   = [];
        let newPreview = [];
        let x = 0;

        // this should only have 1 file, but just go through and create preview of media
        for (let f of _files) 
        {
            newFiles.push(f);
            if (x < previewLoadCount)
            {
                newPreview.push(URL.createObjectURL(f));
                x++;
            }
        }

        setFiles(newFiles);
        setPreview(newPreview);
    }

    // this just checks some basic things so user doesn't forget to add tags, title, or media
    function checkForValidUpload()
    {
        if (title.current.value === "")
        {
            alert("missing title");
            return false;
        }
        if (!files || files.length < 1)
        {
            alert("missing media");
            return false;
        }
        if (searchItems.length === 0)
        {
            alert("missing tags");
            return false;
        }

        return true;
    }

    async function beginUpload(e)
    {
        // stop page from doing stuff
        e.preventDefault();

        // check if we're good to upload
        if (! await checkForValidUpload())
            return;

        // upload each file to api as a post, with data given in forms
        for (let f of files)
        {
            const data = new FormData();

            data.append("data", f);
            data.append("title", title.current.value);
            data.append("description", description.current.value);

            const totalSize = f.size;
            const uploadFile = f.name;
            
            function progress(event)
            {
                const loaded = event.loaded >= totalSize ? totalSize : event.loaded;
                setProgress(`${uploadFile}: uploaded ${loaded} / ${totalSize} bytes`);
            }

            let pots_id = -1;
            await postData(API_TEMPLATES.upload_post.url, data, {}, progress)
            .then(response => JSON.parse(response.response))
            .then(json => 
            {
                console.log(json);

                pots_id = json.post_id;
                if(json.files.length === 0)
                {
                    console.log("Upload responded with 0 files json, skipping adding tags");
                    return;
                }


                // add tags to files/posts
                for(const sub_json of json.files)
                {
                    const url = formatStringB(API_TEMPLATES.add_tag_to_file.url, sub_json.hash);
                    console.log(url);
                    return fetch(url, {
                        method: "post",
                        "headers" : {
                            "Content-Type" : "application/json"
                        },
                        body:  JSON.stringify(searchItems)
                    });
                }})
                .then(response => {
                    console.log("Adding tag status " + response.status);
                })
                
                .catch(err => console.log(err));


                            
            if(pots_id !== -1)
            {
                window.location.href = '/media?post=' + pots_id;
            }
        }
    }

    return (

        <div className="flex flex-col h-screen">
            {/* create header */}
        <HeaderBar toggleSidebarVisibility={()=>setSidebarVisible(!sidebarVisible)} />
        <div className="flex flex-row flex-1">
            {/* create sidebar */}
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
                {/* display stuff in a resizing grid */}
                <div className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-8 bg-button-depressed m-24 px-8 pt-2 pb-4 rounded-3xl">
                    {/* table with submission data */}
                    <table className="py-8 static inline-block">

                        <tbody>
                    <tr><td><p className="text-xl font-bold">Title</p></td></tr>
                    <tr><td><input type="text" placeholder="Title" className="w-[512px] h-8 text-xl" ref={title}/></td></tr>
                    
                    <tr><td><p className="text-xl font-bold pt-4">Description (optional)</p></td></tr>
                    <tr><td><textarea placeholder="Description" className="w-[512px] h-32 text-lg" ref={description}/></td></tr>
                    
                    <tr><td><p className="text-xl font-bold pt-4">Upload Media</p></td></tr>
                    <tr><td><input className="text-custom-white" type="file" accept="image/*,video/*" ref={mediaFile} onChange={generatePreview}/></td></tr>

                    <tr><td>
                        <button 
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                        onClick={beginUpload}>Upload Media
                        </button></td>
                    </tr>
                    </tbody>
                    </table>

                    <div >
                    {
                    // display preview image for file(s) selected
                    files.map((file, index) => {

                        const props = {
                            "link" : filePreviews[index],
                            "hash" : filePreviews[index],
                            "displayType" : DISPLAY_TYPES.thumb_preview,
                            metaData : {
                                id : 0,
                                title : file.name,
                                description : file.name,
                                created_at : 0,
                                mime_int : 0,
                                duration : 0,
                                width : 256,
                                height : 256,
                            }
                        }

                        return <a key={index} 
                        className='border p-4 flex items-center justify-around inline-block '>
                            {/* use media container for preview */}
                            <MediaContainer {...props} />
                            
                            <div >
                                <p className='text-ellipsis wrap'>{file.name}</p>
                            </div>
                        </a>;
                    })
                   }


                </div>
                {/* display progress, in center bottom of the page */}
                <div className="col-span-full text-center">
                    {
                        function(){

                            if(displayProgress === "" || !displayProgress)
                            {
                                return ;
                            }
                            return <p>{displayProgress}</p>;
                        }()
                    }
                </div>

                </div>
                
            </main>
        </div>
</div>
    )
}