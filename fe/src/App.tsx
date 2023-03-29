import { BrowserRouter } from "react-router-dom";
import Layout from "./components/Layout/Layout";
import RoutesComponent from "./components/Layout/RoutesComponent";

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <RoutesComponent />
      </Layout>
    </BrowserRouter>
  );
}

export default App;
