import React from 'react';

import VideoPlayer from '../elements/Video';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;

    const vprops = {
        m3u8: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/5d4256aa61a2a4ef8ee62031f0ae699e90912579449f7ac45a0b154ef420b349",
        domain: "http://localhost:8080/FinalProject-1.0-SNAPSHOT/api/files/5d4256aa61a2a4ef8ee62031f0ae699e90912579449f7ac45a0b154ef420b349/"
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