import React from "react";
import '../login.css'

const SignUpForm = () => {
    return <div>
        <button id="button_back" className="button-login" onClick={() => {
            window.history.back();
        }} style={{right: '1900px'}}>Back
        </button>
        <div className="login-form">
            <div className="login-subtitle">Enter your email and password:</div>
            <div className="input-container ic2">
                <input id="email" className="input" type="text" placeholder=" "/>
                <div className="cut cut-short"></div>
                <label form="email" className="placeholder">Email</label>
            </div>
            <div className="input-container ic2">
                <input id="password" className="input" type="password" placeholder=" "/>
                <div className="cut"></div>
                <label form="password" className="placeholder">Password</label>
            </div>
            <button type="text" className="login-submit" onClick={sendData}>Submit</button>
        </div>
        <div id="errorList" className="error-list" style={{display: 'none'}}/>
    </div>
}

const sendData = () => {
    const password = document.getElementById("password").value;
    const email = document.getElementById("email").value;
    fetch(`http://localhost:8080/users/auth`, {
        method: "POST",
        body: JSON.stringify({
            password: password,
            email: email
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then(response => {
        if (response.ok) {
            response.json().then(responseJson => {
                window.sessionStorage.setItem('token', responseJson['token']);
            });
            console.log(window.sessionStorage.getItem('token'));
            window.location.href = '/users';
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
        const li = document.createElement('div');
        li.className = 'error-list-li';
        li.innerText = str;
        errorList.appendChild(li);
    }
    errorList.style.display = 'block';
    errorList.removeChild(errorList.lastChild);
}

export default SignUpForm