import React from 'react';

import VideoPlayer from '../elements/Video';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const vprops = {
        // "8aab4db0077d8a4f1a870a81908eac54f23ffeead3b5cc2ee55365820dafa72f"
        // m3u8: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/5d4256aa61a2a4ef8ee62031f0ae699e90912579449f7ac45a0b154ef420b349",
        // domain: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/5d4256aa61a2a4ef8ee62031f0ae699e90912579449f7ac45a0b154ef420b349/"
        m3u8: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/ac1794db87d07033c6e398d1e7dcbfac5c3f0bff5b6fe0b7d6b51e1399278e25",
        domain: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/ac1794db87d07033c6e398d1e7dcbfac5c3f0bff5b6fe0b7d6b51e1399278e25/"
    };

    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    function getData(url)
    {
        fetch(url)
        .catch((err) => 
        {
            console.log("something went wrong: " + err);
        })
        .then(x =>
        {
            console.log("loaded data from " + url);

            x.json()
                .then(i =>
                    {
                        return i.mime;
                    })
                    .catch(e => console.log(e));
        })
    }
    const URL = "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/ac1794db87d07033c6e398d1e7dcbfac5c3f0bff5b6fe0b7d6b51e1399278e25";
    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-100 to-gray-300">
            <center>
                <h1 className="pt-6 text-xl">
                    This will be the Media page. {getData(URL)}
                </h1>
                <img src="http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/ac1794db87d07033c6e398d1e7dcbfac5c3f0bff5b6fe0b7d6b51e1399278e25" width="250"></img>
                <VideoPlayer {...vprops}></VideoPlayer>

                <p>
                    i.e. where you will be able to view video, images, or audio
                </p>
            </center>
        </div>
    )
}