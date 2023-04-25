import { BrowserRouter } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import RoutesComponent from "./components/Layout/RoutesComponent";
import NofiticationCenter from "components/Layout/NotificationCenter";
import { AxiosInterceptor } from "api/axios";

function App() {
  return (
    <BrowserRouter>
      <AxiosInterceptor>
        <Layout>
          <RoutesComponent />
          <NofiticationCenter />
        </Layout>
      </AxiosInterceptor>
    </BrowserRouter>
  );
}

export default App;
