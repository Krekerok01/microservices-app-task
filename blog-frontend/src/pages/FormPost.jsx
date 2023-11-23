import React from "react";
import UserInfoComponent from "../component/UserInfoComponent";

const FormPost = () => {
    const sendData = () => {
        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/posts`, {
            method: "POST",
            body: JSON.stringify({
                title: title,
                text: content
            }),
            headers: {
                "Content-type": "application/json; charset=UTF-8",
                "Authorization": `Bearer ${token}`
            }
        }).then(response => {
            if (response.ok) {
                window.location.href = '/myPage';
            } else if (response.status === 400) {
            //     add error handling here
            }
        });
    }
    return <div className="div-form">
        <UserInfoComponent/>
        <button id="button_back" className="button-login" onClick={() => {
            window.history.back();
        }} style={{right: '1810px'}}>Back
        </button>
        <form id="div-form" action="/" className="decor">
            <div className="form-left-decoration"></div>
            <div className="form-right-decoration"></div>
            <div className="circle"></div>
            <div className="form-inner">
                <h1>Creating a post</h1>
                <input id="title" type="text" placeholder="Title"/>
                <textarea id="content" placeholder="Content..." rows="5"></textarea>
                <button className="button-submit" type="submit" onClick={sendData}>Submit
                </button>
            </div>
        </form>
    </div>
}



export default FormPost