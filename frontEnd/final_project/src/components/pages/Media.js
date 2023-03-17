import React from 'react';

import VideoPlayer from '../elements/Video';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const vprops = {
        m3u8: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/1073d143a1848e66c9a093eeca19ba6c76608c150a056b7208fc8c369ee1a386",
        domain: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/1073d143a1848e66c9a093eeca19ba6c76608c150a056b7208fc8c369ee1a386/"
    };

    React.useEffect(() => 
    {
        console.log(`Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    return (
        <div>
            <center>
                <h1 class="pt-6 text-xl">
                    This will be the Media page.
                </h1>

                <VideoPlayer {...vprops}></VideoPlayer>

                <p>
                    i.e. where you will be able to view video, images, or audio
                </p>
            </center>
        </div>
    )
}