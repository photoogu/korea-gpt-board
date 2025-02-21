/**@jsxImportSource @emotion/react */
import * as s from './style';
import { basicButton } from '../../../styles/buttons';
import { FiChevronsLeft } from "react-icons/fi";
import { useRecoilState } from 'recoil';
import { mainSidebarIsOpenState } from '../../../atoms/mainSidebar/mainSidebarAtom';

function MainSidebar(props) {
    const [ isOpen, setOpen ] = useRecoilState(mainSidebarIsOpenState);
    
    const handleSidebarClose = () => {
        setOpen(false);
    }

    return (
        <div css={s.layout(isOpen)}>
            <div css={s.container}>
                <div>
                    {
                        isOpen &&
                        <button css={basicButton} onClick={handleSidebarClose}><FiChevronsLeft /></button>
                    }
                </div>
            </div>
        </div>
    );
}

export default MainSidebar;