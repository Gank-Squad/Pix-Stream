import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import { API_ENDPOINTS } from '../../constants';
import ImageContainer from '../elements/Image';
import VideoPlayer from '../elements/Video';

import postData from '../../requests';

export default function Default(props)
{
    const { cookies } = props;

    const API = API_ENDPOINTS.search.get_files_with_tags;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [files       , setFiles  ] = React.useState([]);
    const [filePreviews, setPreview] = React.useState([]);
    const [messageBox  , setMessage] = React.useState([]);

    const [images, setImageData] = React.useState([]);

    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

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
        "searchCallback" : searchCallback
    }

    const mediaFile = React.useRef("");
    const subFile = React.useRef("");
    const title = React.useRef("");


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

    function renderPreview()
    {
        let r = []
        for(let i = 0; i < Math.min(files.length, 5); i++)
        {
            
            let props = {}
            if (files[i].type == "image/png" || files[i].type == "image/jpg" )
            {
                props = {
                    image    : filePreviews[i],
                    caption  : files[i].name,
                    fileType : files[i].type
                    // onClick : (e) => removePreviewClick(e, i),
                }

                r.push(<ImageContainer {...props} key={i}></ImageContainer>);
            }
            else
            {
                props = {
                    m3u8: API_ENDPOINTS.media.get_file + "C38028E6C58EF639317C357F71976EDD66FF6524A63D590CF213D3562873FE21",
                    domain: API_ENDPOINTS.media.get_file + "C38028E6C58EF639317C357F71976EDD66FF6524A63D590CF213D3562873FE21/"
                }

                r.push(<VideoPlayer {...props}></VideoPlayer>);
            }
            
        }
        return r;
    }

    async function beginUpload(e)
    {
        e.preventDefault();

        if (!files || files.length < 1)
        {
            alert("You gotta add a file >:(")
            return;
        }

        for (let f of files)
        {
            const data = new FormData();

            data.append("data", f);

            fetch(API_ENDPOINTS.media.upload_file, {
            method: "post",

            body: data
            })
        }
        

    }

    return (
        <div class="flex flex-col h-screen overflow-auto">

            {/* This should be a modified version to remove search
                So that user can select the tags their upload will have
            */}
            <nav
                class="group fixed top-20 left-0 h-screen w-60 -translate-x-60 overflow-y-auto overflow-x-hidden shadow-[0_4px_12px_0_rgba(0,0,0,0.07),_0_2px_4px_rgba(0,0,0,0.05)] data-[te-sidenav-hidden='false']:translate-x-0 dark:bg-custom-dark-blue"
                data-te-sidenav-init
                data-te-sidenav-hidden="false"
            >
                <ul class="relative m-0 list-none px-[0.2rem]" data-te-sidenav-menu-ref>
                    <TagSidebar {...tagSidebarProps} />
                </ul>
            </nav>

            <header class="w-full p-4 h-20 bg-custom-dark-blue fixed">
                <table>
                <td><div class="text-left inline-block">
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l" onClick={redirect_home}>Home</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" onClick={redirect_tags}>Tags</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" disabled onClick={redirect_upload}>Upload</button>
                </div></td>

                <td class="center"><div class="text-center inline"><p class="text-xl font-bold">PixStream</p></div></td>
                </table>
            </header>

            <main class="py-24 px-72 static w-full " >
                

                <table class="py-8 static inline-block">
                <tr><p class="text-xl font-bold">Title</p></tr>
                <tr><input type="text" placeholder="Title" class="w-[512px] h-8 text-xl" ref={title}/></tr>
                
                <tr><p class="text-xl font-bold pt-4">Description (optional)</p></tr>
                <tr><textarea placeholder="Description" class="w-[512px] h-32 text-lg"/></tr>
                
                <tr><p class="text-xl font-bold pt-4">Upload Media</p></tr>
                <tr><input class="text-custom-white" type="file" accept="image/*,video/*" ref={mediaFile} onChange={generatePreview}/></tr>

                <tr><p class="text-xl font-bold pt-4">Upload Subtitle Track</p></tr>
                <tr><input class="text-custom-white pb-4" type="file" accept=".srt" ref={subFile}/></tr>

                <tr>
                    <button 
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                    onClick={beginUpload}>Upload Media
                    </button>
                </tr>
                </table>

                <div class="relative w-[60%] px-[5%] aspect-video inline-block">
                    {renderPreview()}
                </div>
                
                
            </main>
            
        </div>
    )
}