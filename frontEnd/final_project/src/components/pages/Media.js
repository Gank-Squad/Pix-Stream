import React from 'react';

export default function Default(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;


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

                <p>
                    i.e. where you will be able to view video, images, or audio
                </p>
            </center>
        </div>
    )
}