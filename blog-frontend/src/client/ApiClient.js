class ApiClient {
    static SERVER_URL = 'http://localhost:8080';
    static GET_POSTS = '/posts'
    static GET_NEWS = '/news'
    static GET_JOBS = '/jobs'
    static GET_USERS = '/users'
    static GET_POSTS_FOR_USER = '/subscriptions'

    static posts(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_POSTS)
            .catch(() => {
                throw new DOMException()
            });
    }

    static news(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_NEWS)
            .catch(() => {
                throw new DOMException()
            });
    }

    static jobs(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_JOBS)
            .catch(() => {
                throw new DOMException()
            });
    }

    static users(): Promise<Response> {
        const token = window.sessionStorage.getItem('token');
        console.log(token);

        return fetch(
            this.SERVER_URL + this.GET_USERS,
            {
                headers: {
                    "Content-type": "Application/json",
                    "Authorization": `Bearer ${token}`
                }
            }).catch(() => {
            throw new DOMException();
        })
    }

    static userPosts(): Promise<Response> {
        const token = window.sessionStorage.getItem('token');
        console.log(token);

        return fetch(
            this.SERVER_URL + this.GET_POSTS + this.GET_POSTS_FOR_USER,
            {
                headers: {
                    "Content-type": "Application/json",
                    "Authorization": `Bearer ${token}`,
                }
            }
        ).catch(() => {
            throw new DOMException()
        });
    }

    static onePost(id)
        :
        Promise<Response> {
        return fetch(
            this.SERVER_URL + this.GET_POSTS + '/' + id,
            {
                headers: {
                    "Content-type": "Application/json"
                }
            }
        );
    }

    static oneUser(id)
        :
        Promise<Response> {
        const token = window.sessionStorage.getItem('token');
        return fetch(
            this.SERVER_URL + this.GET_USERS + '/' + id,
            {
                headers: {
                    "Content-type": "Application/json",
                    "Authorization": `Bearer ${token}`
                }
            }
        );
    }
}


export default ApiClient