import { Global } from "@emotion/react"
import { global } from "./styles/global"
import MainLayout from "./components/common/MainLayout/MainLayout"
import { Route, Routes } from "react-router-dom"
import Authroute from "./routes/AuthRoute/AuthRoute"
import MainRoute from "./routes/MainRoute/MainRoute"
import { useUserMeQuery } from "./queries/userQuery"

function App() {

	useUserMeQuery(); // 최초의 한 번 캐싱 처리

	return (
		<>
			<Global styles={global} />
			<MainLayout>
				<Routes>
					<Route path="/auth/*" element={<Authroute />} />
					<Route path="/*" element={<MainRoute />} />
				</Routes>
			</MainLayout>
		</>
	)
}

export default App
