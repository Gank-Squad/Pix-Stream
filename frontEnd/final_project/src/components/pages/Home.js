import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';

export default function Default(props)
{
    const { cookies } = props;

    const API = "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/tags/files/?tags=true"
    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const [images, setImageData] = React.useState([]);


    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
        console.log("images changed");
    }, [cookies, page, images]);

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


    const tagSidebarProps = {
        "searchCallback" : searchCallback
    }

    return (

        <div class="flex flex-col h-screen overflow-hidden">
            <header class="w-full p-4 h-20 bg-custom-dark-blue">
                <div>
                <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded" disabled>
                    {/* <img src="https://www.svgrepo.com/show/487437/hamburger-menu.svg" class=""/> */}
                    ham
                </button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-l" disabled>Home</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 border border-blue-700 px-4" onClick={redirect_tags}>Tags</button>
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded-r" onClick={redirect_upload}>Upload</button>
                </div>
            </header>

            {/* This is the sidebar/tag search */}
            <nav
            // bg-custom-dark-blue w-64 text-clip
                class="fixed top-20 left-0 h-full bg-custom-dark-blue w-64 text-clip"
            >
                <TagSidebar {...tagSidebarProps}></TagSidebar>

                {images.map((json, index) => 
                {
                    const imgProp = {
                        "image" : `http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/${json.sha256}`,
                        "caption": json.mime,
                        "style" : {
                            width : "200px",
                            border : "1px solid white",
                            // "verticalAlign" : "middle",

                            // "display": "flex",
                            // width: "195px",
                            // height: "185px",
                            // "margin-top": "20px",
                            // "align-items": "center",
                            // "justify-content": "center"
                        }
                    }
                    return <ImageContainer key={index} {...imgProp} imgError={e => console.log("image errr")}></ImageContainer>;
                })}

            </nav>

            <main class="flex-1 overflow-y-scroll px-80 py-12">
            {/* PUT ALL DISPLAY STUFF IN HERE, ANYTHING OUTSIDE MAY NOT BE FORMATED CORRECTLY */}

           

<<<<<<< HEAD

                <p>Hello world, you are on home page {(page).toString()}</p>

            </main>
=======
>>>>>>> 06b5d661bc41a8dfb34361c3cd7a6f1fc23f66a2
        </div>
    )
}