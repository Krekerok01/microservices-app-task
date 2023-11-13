class ApiClient {
    static SERVER_URL = 'http://localhost:8080';
    static GET_POSTS = '/posts'

    static posts(): Promise<Response> {
        return fetch(ApiClient.SERVER_URL + ApiClient.GET_POSTS);
    }
}


export default ApiClient