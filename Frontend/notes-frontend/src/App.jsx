import { Outlet, Link } from "react-router-dom";

export default function App(){
  return (
    <div style={{padding:20}}>
      <nav>
        <Link to="/">Notes</Link> | <Link to="/login">Login</Link>
      </nav>
      <hr/>
      <Outlet />
    </div>
  )
}
