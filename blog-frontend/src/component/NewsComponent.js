import React from "react";
import NewsCard from "./NewsCard";
import ApiClient from "../client/ApiClient";
import JobCard from "./JobCard";

class NewsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            show: true
        }
    }

    componentDidMount(): void {
        this.refreshNewsCards();
        setInterval(this.refreshNewsCards.bind(this), 600000);
    }

    refreshNewsCards() {
        this.getNewsData().then(json => {
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

    getNewsData(): Promise {
        return ApiClient.news().then(
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

    render() {
        let news;
        if(this.state.show === true) {
            news = this.state.data.map(news => {
                return <NewsCard key={news.url} sourceName={news.sourceName} title={news.title} url={news.url} publishedAt={news.publishedAt}/>
            })
        } else {
            news = <div className="loading-container-n" style={{paddingTop: '800px'}}>
                <div className="loading-text-j">
                    <span>L</span>
                    <span>O</span>
                    <span>A</span>
                    <span>D</span>
                    <span>I</span>
                    <span>N</span>
                    <span>G</span>
                    <span style={{paddingLeft: '30px'}}></span>
                    <span>N</span>
                    <span>E</span>
                    <span>W</span>
                    <span>S</span>
                </div>
            </div>
        }
        return <div id="main_news_component" className="news-container">
            {/*{this.state.data.map(news => {*/}
            {/*    return <NewsCard key={news.url} sourceName={news.sourceName} title={news.title} url={news.url} publishedAt={news.publishedAt}/>*/}
            {/*})}*/}
            {news}
        </div>
    }
}

export default NewsComponent