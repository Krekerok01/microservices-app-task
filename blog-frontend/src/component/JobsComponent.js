import React from "react";
import JobCard from "./JobCard";
import ApiClient from "../client/ApiClient";

class JobsComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
    }

    componentDidMount(): void {
        this.refreshJobCards();
        setInterval(this.refreshJobCards.bind(this), 600000);
    }

    refreshJobCards() {
        this.getJobData().then(json => {
                this.setState({
                    data: json
                });
            }
        )
    }

    getJobData(): Promise {
        return ApiClient.jobs().then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return Promise.reject("Job-service: error response");
                }
            }
        )
    }

    render() {
        return <div className="jobs-container">
            {this.state.data.map(job => {
                return <JobCard key={job.url} title={job.title} url={job.url} companyName={job.companyName} jobSource={job.jobSource}/>
            })}
        </div>
    }
}

export default JobsComponent