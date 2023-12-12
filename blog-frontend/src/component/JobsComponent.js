import React from "react";
import JobCard from "./JobCard";
import '../form.css'
import ApiClient from "../client/ApiClient";

class JobsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            show: true
        }
    }

    componentDidMount(): void {
        this.refreshJobCards();
        setInterval(this.refreshJobCards.bind(this), 600000);
    }

    refreshJobCards() {
        this.getJobData().then(json => {
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

    getJobData(): Promise {
        return ApiClient.jobs().then(
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
        let jobs;
        if(this.state.show === true) {
            jobs = this.state.data.map(job => {
                return <JobCard key={job.url} title={job.title} url={job.url} companyName={job.companyName} jobSource={job.jobSource}/>
            });
        } else {
            jobs = <div className="loading-container-j" style={{paddingTop: '300px'}}>
                <div className="loading-text-j">
                    <span>L</span>
                    <span>O</span>
                    <span>A</span>
                    <span>D</span>
                    <span>I</span>
                    <span>N</span>
                    <span>G</span>
                    <span style={{paddingLeft: '30px'}}></span>
                    <span>J</span>
                    <span>O</span>
                    <span>B</span>
                    <span>S</span>
                </div>
            </div>
        }
        return <div id="main_jobs_container" className="jobs-container">
            {/*{this.state.data.map(job => {*/}
            {/*    return <JobCard key={job.url} title={job.title} url={job.url} companyName={job.companyName} jobSource={job.jobSource}/>*/}
            {/*})}*/}
            {jobs}
        </div>
    }
}

export default JobsComponent