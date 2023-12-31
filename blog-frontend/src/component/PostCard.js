import '../App.css';
import React from "react";

class PostCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: props.username,
            title: props.title,
            text: props.text,
            postId: props.postId
        }
    }
    DeleteButton(props) {
        return (
            <button id="delete_btn" className={props.className} style={props.style} onClick={() => {
                const token = window.sessionStorage.getItem('token');
                fetch(`http://localhost:8080/posts/${props.postId}`, {
                    method: "DELETE",
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
                });}}>
                {props.text}
            </button>);
    }

    UpdateButton(props) {
        return (
            <button id="update_btn" className={props.className} style={props.style} onClick={() => {window.location.href = '/updatePost?id=' + props.postId}}>{props.text}</button>
        );
    }

    render() {
        let deleteButton, updateButton;
        const none = {
            display: 'none',
        }
        if (window.sessionStorage.getItem('username') === this.state.username) {
            deleteButton = <this.DeleteButton text="Delete" className="delete-update-button" postId={this.props.postId}/>;
            updateButton = <this.UpdateButton text="Edit" className="delete-update-button" postId={this.props.postId}/>;
        } else {
            deleteButton = <this.DeleteButton text="Delete post" style={none}/>;
            updateButton = <this.UpdateButton text="Edit post" style={none}/>;
        }
        return (
            <div className="card">
                <h4>{this.state.title}</h4>
                <h5>{this.state.text}</h5>
                <h6><em>{this.state.username}</em></h6>
                <div className="button-container">
                    {deleteButton}
                    {updateButton}
                </div>
            </div>
        );
    }
}

export default PostCard