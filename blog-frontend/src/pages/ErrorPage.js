import React from "react";

const ErrorPage = () => {
    return <div className='error-page'>
        <img src="https://cdn.pixabay.com/photo/2021/07/21/12/49/error-6482984_960_720.png" alt="Error" width="500" height="500" style={{display: 'block', margin: 'auto'}}/>
        <h1>The page you are looking for does not exist</h1>
         <button id="button_home" className="button-login" onClick={() => {
                        window.location.href = '/';
                    }} >Home
         </button>
    </div>

}

export default ErrorPage