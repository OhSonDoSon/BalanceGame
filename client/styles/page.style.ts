import styled from "styled-components";

export const IndexPageWrapper = styled.body`
  display: flex;
  margin: 0;
  padding: 0;
  background-color: #6b638f;
  min-height: 100vh;
`;

export const MainPageBody = styled.div`
  width: 100%;
  max-width: 768px;
  margin: 0 auto;
  background-color: #fff; /* 웹페이지 내용의 배경색 */

  @media screen and (min-width: 769px) {
    background-color: #fff; /* 원하는 색상으로 바꿔주세요. 양옆 색상입니다. */
  }
`;
