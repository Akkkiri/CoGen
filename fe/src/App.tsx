import { BrowserRouter } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import RoutesComponent from "./components/Layout/RoutesComponent";
import NofiticationCenter from "components/Layout/NotificationCenter";

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <RoutesComponent />
        <NofiticationCenter />
      </Layout>
    </BrowserRouter>
  );
}

export default App;
