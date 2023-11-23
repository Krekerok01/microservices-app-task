import React from "react";
import '../form.css'
import ApiClient from "../client/ApiClient";
import UserCard from "./UserCard";

class UsersComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    componentDidMount(): void {
        this.refreshUsers();
        setInterval(this.refreshUsers.bind(this), 5000);
        this.checkBackButton();
    }

    checkBackButton() {
        const username = window.sessionStorage.getItem('username');
        if(username != null || username !== 'null') {
            document.getElementById('button_back').style.top = '300px';
        }
    }

    refreshUsers() {
        this.getUsersData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getUsersData(): Promise {
        return ApiClient.users().then(
             response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("User-service: error response");
                }
             }
        )
    }

    render() {

        return <div className="card-grid">
            <button id="button_back" className="button-login" onClick={() => {
                window.history.back();
            }} style={{right: '1870px'}}>Back
            </button>
            {this.state.data.map(user => {
                return <UserCard key={user.id} username={user.username} email={user.email}/>
            })}
        </div>
    }
}

export default UsersComponent