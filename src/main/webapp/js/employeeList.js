window.onload = () =>{

    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');

    document.getElementById("getEmployees").addEventListener("click", getAllEmployees);
    
    getAllEmployees();
}

function getAllEmployees(){
      //AJAX Logic
      let xhr = new XMLHttpRequest();

      xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
             
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            showEmployees(data);
        }
    };
        
    xhr.open("GET",`viewAllEmployees.do?fetch=LIST`);
 
    xhr.send();
}

function showEmployees(data) {
     
    
      if(data.message){
          document.getElementById("listMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
      }else{
 
        let employeeList = document.getElementById("employeeList");
        employeeList.innerHTML="";

        data.forEach((employee)=>{
            
            let tr = document.createElement('tr');  
            let td1 = document.createElement('td'); 
            let td2 = document.createElement('td'); 
            let td3 = document.createElement('td'); 
            let td4 = document.createElement('td'); 
            let td5 = document.createElement('td'); 
         
        
            let txt1 = document.createTextNode(`${employee.id}`);
            let txt2 = document.createTextNode(`${employee.firstName}`);
            let txt3 = document.createTextNode(`${employee.lastName}`);
            let txt4 = document.createTextNode(`${employee.username}`);
            let txt5 = document.createTextNode(`${employee.email}`);
     
            td1.appendChild(txt1);
            td2.appendChild(txt2);
            td3.appendChild(txt3);
            td4.appendChild(txt4);
            td5.appendChild(txt5);
       
            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
        
            employeeList.appendChild(tr);
      });
        
      }
}