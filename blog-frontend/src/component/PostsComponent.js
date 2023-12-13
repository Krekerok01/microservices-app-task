import React from "react";
import PostCard from "./PostCard";
import '../form.css'
import ApiClient from "../client/ApiClient";

class PostsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            show: true
        }
    }

    componentDidMount(): void {
        this.refreshPostCards();
        setInterval(this.refreshPostCards.bind(this), 5000);
    }

    refreshPostCards() {
        this.getPostsData().then(json => {
                this.setState({
                    data: json,
                    show: true
                });
            }
        ).catch(() => {
            this.setState({
                show: false
            });
        })
    }

    getPostsData(): Promise {
        return ApiClient.posts().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new DOMException()
                }
            }
        ).catch(() => {
            throw new DOMException()
        });
    }

    LogInButton(props) {
        return <button id="button_log" onClick={() => {
            window.location.href = '/login';
        }} className="button-login">Log in
        </button>
    }

    SignUpButton(props) {
        return <button id="button_sign_up" onClick={() => {
            window.location.href = '/signup';
        }} className="button-login" style={{top: '60px'}}>Sign up
        </button>
    }

    render() {
        let posts, logInButton, signUpButton;
        if (this.state.show === true) {
            posts = this.state.data.map(post => {
                return <PostCard key={post.postId} username={post.username} title={post.title} text={post.text}
                                 postId={post.postId}/>
            });
        } else {
            posts = <div className="loading-container" style={{paddingTop: '300px'}}>
                <div className="loading-text">
                    <span>L</span>
                    <span>O</span>
                    <span>A</span>
                    <span>D</span>
                    <span>I</span>
                    <span>N</span>
                    <span>G</span>
                    <span style={{paddingLeft: '30px'}}></span>
                    <span>P</span>
                    <span>O</span>
                    <span>S</span>
                    <span>T</span>
                    <span>S</span>
                </div>
            </div>
        }
        console.log('TOKEN ' + window.sessionStorage.getItem('token'));
        if (window.sessionStorage.getItem('token') === 'null' || window.sessionStorage.getItem('token') == null) {
            logInButton = <this.LogInButton style={{display: 'none'}}/>;
            signUpButton = <this.SignUpButton style={{display: 'none'}}/>;
        }
        return <div id="main_posts_container" className="card-grid">
            {signUpButton}
            {logInButton}
            <button id="button_create" className="button-login" onClick={() => {
                window.location.href = '/addPost';
            }} style={{display: 'none', right: '250px'}}>Create post
            </button>
            {posts}
        </div>
    }
}

export default PostsComponent