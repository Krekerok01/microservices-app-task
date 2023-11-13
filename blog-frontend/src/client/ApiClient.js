class ApiClient {
    static SERVER_URL = 'http://localhost:8080';
    static GET_POSTS = '/posts'
    static GET_NEWS = '/news'

    static posts(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_POSTS);
    }

    static news(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_NEWS);
    }
}


export default ApiClient