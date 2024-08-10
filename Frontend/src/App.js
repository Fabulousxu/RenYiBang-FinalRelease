import {Navigate, Route, Routes} from 'react-router-dom';
import TaskPage from "./page/task_page";
import ServicePage from "./page/service_page";
import MessagePage from "./page/message_page";
import ProfilePage from "./page/profile_page";
import OrderPage from "./page/order_page";
import IssuePage from './page/issue_page';
import TaskDetailPage from "./page/task_detail_page";
import ServiceDetailPage from "./page/service_detail_page";
import OrderRecipientPage from "./page/order_recipient_page";
import OrderInitiatorPage from "./page/order_initiator_page";
import SelectPage from './page/select_page';
import LoginPage from "./page/login_page";
import UserPage from "./page/user_page";

export default function App() {
  return (
    <Routes>
      <Route index={true} element={<Navigate to='/login'/>}/>
      <Route path='/task' element={<TaskPage/>}/>
      <Route path='/task/:id' element={<TaskDetailPage/>}/>
      <Route path='/service' element={<ServicePage/>}/>
      <Route path='/service/:id' element={<ServiceDetailPage/>}/>
      <Route path='/order' element={<OrderPage/>}/>
      <Route path='/order/task/recipient/:id' element={<OrderRecipientPage/>}/>
      <Route path='/order/service/recipient/:id' element={<OrderRecipientPage/>}/>
      <Route path='/order/task/initiator/:id' element={<OrderInitiatorPage/>}/>
      <Route path='/order/service/initiator/:id' element={<OrderInitiatorPage/>}/>
      <Route path='/message' element={<MessagePage/>}/>
      <Route path='/profile/:id' element={<UserPage/>}/>
      <Route path='/profile/self' element={<ProfilePage/>}/>
      <Route path='/issue' element={<IssuePage/>}/>
      <Route path='/select/task/:id' element={<SelectPage/>}/>
      <Route path='/select/service/:id' element={<SelectPage/>}/>
      <Route path='/login' element={<LoginPage/>}/>
    </Routes>
  );
}