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
            console.log('Response status after adding:' + response.status);
            if (response.ok) {
                window.location.href = '/myPage';
            } else if (response.status === 400) {
                response.json().then(responseJson => {
                    showError(responseJson['message']);
                });
            }
        });
    }

    const showError = (message) => {
        const errors = message.split(";");
        console.log(message);
        const errorList = document.getElementById('errorList');
        while (errorList.firstChild) {
            errorList.removeChild(errorList.firstChild);
        }
        for (const str of errors) {
            console.log(str);
            const li = document.createElement('div');
            li.className = 'error-list-add';
            li.innerText = str;
            errorList.appendChild(li);
        }
        errorList.style.display = 'block';
        window.scrollTo(0, document.body.scrollHeight);
    }


    return <div className="div-form">
        <UserInfoComponent/>
        <button id="button_back" className="button-login" onClick={() => {
            window.history.back();
        }} style={{right: '1810px'}}>Back
        </button>
        <button id="button_home" className="button-login" onClick={() => {
            window.location.href = '/';
        }} style={{right: '1710px'}}>Home
        </button>
        <div className="form">
            <div className="form-left-decoration"></div>
            <div className="form-right-decoration"></div>
            <div className="circle"></div>
            <div className="form-inner">
                <h1 className="form-h1">Creating a post</h1>
                <input id="title" type="text" placeholder="Title" className="form-input"/>
                <textarea id="content" placeholder="Content..." rows="5" className="form-textarea"></textarea>
                <button className="button-submit" type="submit" onClick={sendData}>Submit</button>
            </div>
            <div id="errorList" className="error-list-add-outer" style={{display: 'none'}}/>
        </div>
    </div>
}


export default FormPost