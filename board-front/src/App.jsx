import { Global } from "@emotion/react"
import { global } from "./styles/global"
import MainLayout from "./components/common/MainLayout/MainLayout"
import MainContainer from "./components/common/MainContainer/MainContainer"
import MainSidebar from "./components/common/MainSidebar/MainSidebar"

function App() {

	return (
		<>
			<Global styles={global} />
			<MainLayout>
				<MainSidebar />
				<MainContainer>

				</MainContainer>
			</MainLayout>
		</>
	)
}

export default App
