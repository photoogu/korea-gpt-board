/**@jsxImportSource @emotion/react */
import { useRecoilState } from 'recoil';
import { basicButton } from '../../../styles/buttons';
import * as s from './style';
import { mainSidebarIsOpenState } from '../../../atoms/mainSidebar/mainSidebarAtom';
import { FiChevronsRight } from 'react-icons/fi';

function MainContainer({ children }) {
    const [ isOpen, setOpen ] = useRecoilState(mainSidebarIsOpenState);

    const handleSidebarOpen = () => {
        setOpen(true);
    }

    return (
        <div css={s.container}>
            {
                !isOpen && 
                <button css={basicButton} onClick={handleSidebarOpen}><FiChevronsRight /></button>
            }
            {children}
        </div>
    );
}

export default MainContainer;