import React from 'react';

import TagSidebar from '../elements/TagSidebar';
import ImageContainer from '../elements/Image';

export default function Default(props)
{
    const { cookies } = props;

    const API = "http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/tags/files/?tags=true"
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


    const tagSidebarProps = {
        "searchCallback" : searchCallback
    }

    return (
        <div>

            Hello world, you are on home page {(page).toString()}

            <TagSidebar {...tagSidebarProps}></TagSidebar>

            {images.map((json, index) => 
            {
                const imgProp = {
                    "image" : `http://192.168.1.148:8080/FinalProject-1.0-SNAPSHOT/api/files/${json.sha256}`,
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

        </div>
    )
}