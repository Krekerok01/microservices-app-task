import React from "react";
import '../login.css'

const SignUpForm = () => {
    return <div>
        <button id="button_back" className="button-login" onClick={() => {
            window.history.back();
        }} style={{right: '1900px'}}>Back
        </button>
        <div className="form">
            <div className="title">Welcome</div>
            <div className="subtitle">Let's create your account!</div>
            <div className="input-container ic1">
                <input id="username" className="input" type="text" placeholder=" "/>
                <div className="cut"></div>
                <label form="username" className="placeholder">Username</label>
            </div>
            <div className="input-container ic2">
                <input id="password" className="input" type="password" placeholder=" "/>
                <div className="cut"></div>
                <label form="password" className="placeholder">Password</label>
            </div>
            <div className="input-container ic2">
                <input id="email" className="input" type="text" placeholder=" "/>
                <div className="cut cut-short"></div>
                <label form="email" className="placeholder">Email</label>
            </div>
            <button type="text" className="submit" onClick={sendData}>Submit</button>
        </div>
        <div id="errorList" className="error-list" style={{display: 'none'}}/>
    </div>
}

const sendData = () => {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const email = document.getElementById("email").value;
    fetch("http://localhost:8081/users", {
        method: "POST",
        body: JSON.stringify({
            username: username,
            password: password,
            email: email,
            role: "DEFAULT"
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then(response => {
        if (response.ok) {
            window.location.href = '/';
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