import React from "react";

const FormPost = () => {
    return <div className="div-form">
        <button id="button_back" className="button-login" onClick={() => {
            window.history.back();
        }} style={{right: '1900px'}}>Back
        </button>
        <form id="div-form" action="/" className="decor">
            <div className="form-left-decoration"></div>
            <div className="form-right-decoration"></div>
            <div className="circle"></div>
            <div className="form-inner">
                <h1>Creating a post</h1>
                <input type="text" placeholder="Title"/>
                <textarea placeholder="Content..." rows="5"></textarea>
                <button className="button-submit" type="submit">Submit
                </button>
            </div>
        </form>
    </div>
}

export default FormPost