import React from 'react';

import VideoPlayer from '../elements/Video';
import { API_ENDPOINTS } from '../../constants';
export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const vprops = {
        m3u8: API_ENDPOINTS.media.get_file + "D2765EC844F9C92DF35152A5725E0ED381221F202B9BDC190DF599942DEFE930",
        domain: API_ENDPOINTS.media.get_file + "D2765EC844F9C92DF35152A5725E0ED381221F202B9BDC190DF599942DEFE930/"
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