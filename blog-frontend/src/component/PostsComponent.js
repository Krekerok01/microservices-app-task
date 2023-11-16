import React from "react";
import PostCard from "./PostCard";
import ApiClient from "../client/ApiClient";

class PostsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            flag: false
        }
    }

    componentDidMount(): void {
        this.refreshPostCards();
        setInterval(this.refreshPostCards.bind(this), 5000);
    }

    refreshPostCards() {
        this.getPostsData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getPostsData(): Promise {
        return ApiClient.posts().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("Post-service: error response");
                }
            }
        )
    }
    render() {
        return <div className="card-grid">
            <button id="button_log" onClick={() => {
                if(this.state.flag === false) {
                    const btn = document.getElementById("button_create");
                    btn.style.display = "block";
                    const btn1 = document.getElementById("button_log");
                    btn1.textContent = "Log out";
                    this.setState({
                        flag: true
                    });
                } else {
                    const btn = document.getElementById("button_create");
                    btn.style.display = "none";
                    const btn1 = document.getElementById("button_log");
                    btn1.textContent = "Log in";
                    this.setState({
                        flag: false
                    });
                }
            }} className="button-login">Log in</button>
            <button id="button_create" className="button-create" style={{display: 'none'}}>Create post</button>
            {this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}/>
            })}
        </div>
    }
}

export default PostsComponent