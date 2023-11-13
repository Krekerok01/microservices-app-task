import React from "react";
import NewsCard from "./NewsCard";
import ApiClient from "../client/ApiClient";

class NewsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    componentDidMount(): void {
        this.refreshNewsCards();
        setInterval(this.refreshNewsCards.bind(this), 60000);
    }

    refreshNewsCards() {
        this.getNewsData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getNewsData(): Promise {
        return ApiClient.news().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("News-service: error response");
                }
            }
        )
    }

    render() {
        return <div className="news-grid">
            {this.state.data.map(news => {
                return <NewsCard key={news.url} sourceName={news.sourceName} title={news.title} url={news.url} publishedAt={news.publishedAt}/>
            })}
        </div>
    }
}

export default NewsComponent