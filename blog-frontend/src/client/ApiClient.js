import axios from 'axios'

class ApiClient {
    static SERVER_URL = 'http://localhost:8080';
    static GET_POSTS = '/posts'
    static GET_NEWS = '/news'
    static GET_JOBS = '/jobs'
    static GET_USERS = '/users'
//    static PROXY_URL = 'https://corsproxy.io/?' + encodeURIComponent('http://localhost:8080'); //!!!!!!!!!!!!!!!!!!!!!!!!!

    static posts(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_POSTS);
    }

    static news(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_NEWS);
    }

    static jobs(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_JOBS);
    }

    static users(): Promise<Response> {
        const token = window.sessionStorage.getItem('token');
        console.log(token);

        return axios.get(
            this.SERVER_URL + this.GET_USERS,
            {headers: {
                    "Content-type": "Application/json",
                    "Authorization": `Bearer ${token}`,
                    }
                }
          )
    }
}


export default ApiClient