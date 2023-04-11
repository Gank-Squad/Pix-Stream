import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import { API_ENDPOINTS, API_TEMPLATES } from '../../constants';
import { formatStringB } from '../../requests';
import ImageContainer from '../elements/Image';
import VideoPlayer from '../elements/Video';
import HeaderBar from '../elements/HeaderBar';

import postData from '../../requests';

export default function Default(props)
{
    const { cookies } = props;

    const API = API_ENDPOINTS.search.get_files_with_tags;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [searchItems , setSearchItems] = React.useState([]);
    const [files       , setFiles  ] = React.useState([]);
    const [filePreviews, setPreview] = React.useState([]);
    const [displayProgress  , setProgress] = React.useState("");
    const [sidebarVisible, setSidebarVisible] = React.useState(true);

    const [images, setImageData] = React.useState([]);

    const mediaFile = React.useRef("");
    const subFile = React.useRef("");
    const title = React.useRef("");
    const description = React.useRef("");


    function searchCallback(searchItems)
    {
        setSearchItems(searchItems);

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
        "hideSearchButton" : true,
        "searchButtonPressed" : searchButtonPressed,
    }


    function searchButtonPressed()
    {
        window.location.href = '/results?tags=';
    }

    function generatePreview()
    {
        if (!mediaFile.current.files || mediaFile.current.files.length === 0)
        {
            return;
        }

        const _files = Array.from(mediaFile.current.files);

        let newFiles   = [];
        let newPreview = [];
        let x = 0;

        for (let f of _files) 
        {
            newFiles.push(f);

            if (x < 5)
            {
                newPreview.push(URL.createObjectURL(f));
                x++;
            }
        }

        setFiles(newFiles);
        setPreview(newPreview);
    }

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
        e.preventDefault();

        if (! await checkForValidUpload())
            return;

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

            await postData(API_TEMPLATES.upload_post.url, data, {}, progress)
            .then(response => JSON.parse(response.response))
            .then(json => 
            {
                console.log(json);

                if(json.files.length === 0)
                {
                    console.log("Upload responded with 0 files json, skipping adding tags");
                    return;
                }

                for(const sub_json of json.files)
                {
                    const url = formatStringB(API_TEMPLATES.add_tag_to_file.url, sub_json.hash);
                    console.log(url);
                    fetch(url, {
                        method: "post",
                        "headers" : {
                            "Content-Type" : "application/json"
                        },
                        body:  JSON.stringify(searchItems)
                    }).then(response => {
                        console.log("Adding tag status " + response.status);
                    })
                }
                
                })
                .catch(err => console.log(err));
        }
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
            <table className="py-8 static inline-block">
                <thead>
                    <tr>
                        <td>
                            
                   {
                    function(){

                        if(displayProgress === "" || !displayProgress)
                        {
                            return ;
                        }
                        return <p>{displayProgress}</p>;
                    }()
                   }
                        </td>
                    </tr>
                </thead>
                    <tbody>
                <tr><td><p className="text-xl font-bold">Title</p></td></tr>
                <tr><td><input type="text" placeholder="Title" className="w-[512px] h-8 text-xl" ref={title}/></td></tr>
                
                <tr><td><p className="text-xl font-bold pt-4">Description (optional)</p></td></tr>
                <tr><td><textarea placeholder="Description" className="w-[512px] h-32 text-lg" ref={description}/></td></tr>
                
                <tr><td><p className="text-xl font-bold pt-4">Upload Media</p></td></tr>
                <tr><td><input className="text-custom-white" type="file" multiple="multiple" accept="image/*,video/*" ref={mediaFile} onChange={generatePreview}/></td></tr>

                <tr><td><p className="text-xl font-bold pt-4">Upload Subtitle Track</p></td></tr>
                <tr><td><input className="text-custom-white pb-4" type="file" accept=".srt" ref={subFile}/></td></tr>

                <tr><td>
                    <button 
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                    onClick={beginUpload}>Upload Media
                    </button></td>
                </tr>
                </tbody>
                </table>

                <div className="relative w-[60%] px-[5%] aspect-video inline-block">
{
                    files.map((file, i) => {

                        let props = {}

                        if (file.type.startsWith("image/"))
                        {
                            props = {
                                image    : filePreviews[i],
                                caption  : file.name,
                                fileType : file.type
                                // onClick : (e) => removePreviewClick(e, i),
                            }

                            return <ImageContainer {...props} key={i}></ImageContainer>;
                        }
                        else
                        {
                            props = {
                                hlsUrl: API_ENDPOINTS.media.get_file + "C38028E6C58EF639317C357F71976EDD66FF6524A63D590CF213D3562873FE21/",
                            }

                            return <VideoPlayer {...props}></VideoPlayer>;
                        }
                    })
                   }


                </div>
                
                
            </main>
        </div>
</div>
    )
}