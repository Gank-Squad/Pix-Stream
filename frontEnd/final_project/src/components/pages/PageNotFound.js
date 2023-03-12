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
                <button class="box-border h-16 w-48 p-4 border-2 hover:box-content" onClick={redirect}>Return Home</button>
            </center>
        </div>
    )
}