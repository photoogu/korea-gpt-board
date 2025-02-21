import { css } from "@emotion/react";

export const layout = (isOpen) => css`
    transition: all 0.5s ease-in-out;
    box-sizing: border-box;

    width: ${isOpen ? "30rem" : "0"};
    height: 100vh;

    background-color: #f8f8f5;
`;

export const container = css`
    width: 30rem;
    height: 100vh;
`;