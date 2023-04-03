import React from 'react';

export default function Invalid(props)
{
    const { cookies } = props;

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('page')) || 1;


    React.useEffect(() => 
    {
        console.log(`404 Page ${page} was loaded!`);
        console.log(`Cookies from props: ${cookies}`);
    }, [cookies, page]);

    function redirect()
    {
        window.location.href = '/home';
    }

    return (
        <div>
            <center>
                <h1 class="pt-6 pb-6 text-xl font-bold">404 unknown page</h1>
                <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={redirect}>Return Home</button>
            </center>
        </div>
    )
}