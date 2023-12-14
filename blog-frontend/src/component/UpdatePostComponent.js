import React from "react";
import UserInfoComponent from "./UserInfoComponent";
import ApiClient from "../client/ApiClient";

class UpdatePostComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            text: "",
            postId: -1
        }
    }

    componentDidMount() {
        const postId = new URLSearchParams(window.location.search).get('id');
        this.getPostForUpdating(postId).then(data => {
            this.setState({
                title: data.title,
                text: data.text,
                postId: postId
            });
        });
    }


    getPostForUpdating(id): Promise {
        return ApiClient.onePost(id).then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("Post-service: error response");
                }
            }
        )
    }


    sendData = () => {
        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;
        const token = window.sessionStorage.getItem('token');
        fetch(`http://localhost:8080/posts/${this.state.postId}`, {
            method: "PATCH",
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
                response.json().then(responseJson => {
                    this.showError(responseJson['message']);
                });
            }
        });
    }

    showError = (message) => {
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


    render() {
        return <div className="div-form">
            <UserInfoComponent/>
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{top: '70px'}}>Back
            </button>
            <button id="button_home" className="button-login" onClick={() => {
                window.location.href = '/';
            }} >Home
            </button>
            <div className="form">
                <div className="form-left-decoration"></div>
                <div className="form-right-decoration"></div>
                <div className="circle"></div>
                <div className="form-inner">
                    <h1 className="form-h1">Updating a post</h1>
                    <input id="title" type="text" placeholder="Title" className="form-input" value={this.state.title} onChange={e => this.setState({ title: e.target.value })}/>
                    <textarea id="content" placeholder="Content..." rows="5" className="form-textarea" value={this.state.text} onChange={e => this.setState({ text: e.target.value })}></textarea>
                    <button className="button-submit-update" type="submit" onClick={this.sendData}>Submit
                    </button>
                </div>
                <div id="errorList" className="error-list-add-outer" style={{display: 'none'}}/>
            </div>
        </div>
    }
}

export default UpdatePostComponent