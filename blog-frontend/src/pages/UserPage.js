import UsersComponent from "../component/UsersComponent";
import '../user.css'
import UserInfoComponent from "../component/UserInfoComponent";

const UserPage = () => {
    return <div>
        <UserInfoComponent/>
        <h1 className='user-h1'>All users</h1>
        <UsersComponent/>
    </div>
}

export default UserPage