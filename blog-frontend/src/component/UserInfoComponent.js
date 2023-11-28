import React from "react";

class UserInfoComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: ""
        }
    }

    componentDidMount() {
        window.onscroll = function() {scrolling()};
        const header = document.getElementById('userHeader');
        const stickyStyle = header.offsetTop;
        function scrolling() {
            if(window.pageYOffset > stickyStyle) {
                header.classList.add(stickyStyle);
            } else {
                header.classList.remove(stickyStyle);
            }
        }
        const username = window.sessionStorage.getItem('username');
        if(username == null || username === 'null') {
            header.style.display = 'none';
            document.getElementById('logout_btn').style.display='none';
        } else {
            this.setState({
                username: username
            });
            document.getElementById('logout_btn').style.display='block';
        }
    }

    logout = () => {
        window.sessionStorage.setItem('username', null);
        window.sessionStorage.setItem('token', null);
        window.location.href='/';
    }



    render() {
        return <div className="user-header" id="userHeader">
            <img src="https://th.bing.com/th/id/R.bae2d37c4317140a408aef6671346186?rik=X1vYbxH6nQxCcA&riu=http%3a%2f%2fcdn.onlinewebfonts.com%2fsvg%2fimg_218090.png&ehk=poXsiWmpbb3%2b%2bK%2blj8H9AQprCYsoz4kt%2bU4rFFKbOCo%3d&risl=&pid=ImgRaw&r=0" alt="Image"/>
            <h2 onClick={() => {window.location.href = '/myPage'}}>{this.state.username}</h2>
            <button id="logout_btn" onClick={this.logout} style={{display: 'none'}}>Log out</button>
        </div>
    }
}

export default UserInfoComponent