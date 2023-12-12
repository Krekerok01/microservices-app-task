import React from "react";
import '../form.css'
import ApiClient from "../client/ApiClient";
import UserCard from "./UserCard";

class UsersComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            show: true
        }
    }

    componentDidMount(): void {
        this.refreshUsers();
        setInterval(this.refreshUsers.bind(this), 5000);
        this.checkBackButton();
    }

    checkBackButton() {
        const username = window.sessionStorage.getItem('username');
        if (username != null || username !== 'null') {
            document.getElementById('button_back').style.top = '300px';
        }
    }

    refreshUsers() {
        this.getUsersData().then(json => {
                this.setState({
                    data: json,
                    show: true
                });
            }
        ).catch(() => {
            this.setState({
                show: false
            });
        });
    }

    getUsersData(): Promise {
        return ApiClient.users().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else if (response.status === 401) {
                    window.location.href = '/login';
                } else {
                    throw new DOMException()
                }
            }
        ).catch(() => {
            throw new DOMException()
        });
    }

    render() {
        let users;
        const currentUserId = window.sessionStorage.getItem('currentUserId');
        if (this.state.show === true) {
            users = this.state.data.map(user => {
                return <UserCard key={user.id} username={user.username} email={user.email} userId={user.id}
                                 role={user.role} currentUserId={currentUserId}/>
            });
        } else {
            users = <div className="loading-container" style={{paddingTop: '800px'}}>
                <div className="loading-text">
                    <span>L</span>
                    <span>O</span>
                    <span>A</span>
                    <span>D</span>
                    <span>I</span>
                    <span>N</span>
                    <span>G</span>
                    <span style={{paddingLeft: '30px'}}></span>
                    <span>U</span>
                    <span>S</span>
                    <span>E</span>
                    <span>R</span>
                    <span>S</span>
                </div>
            </div>
        }
        return <div className="card-grid">
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{top: '100px'}}>Back
            </button>
            {/*{this.state.data.map(user => {*/}
            {/*    return <UserCard key={user.id} username={user.username} email={user.email} userId={user.id} role={user.role} currentUserId={currentUserId}/>*/}
            {/*})}*/}
            {users}
        </div>
    }
}

export default UsersComponent