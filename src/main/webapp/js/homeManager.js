window.onload=()=>{
    
    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');
     
    getEmployee();
}

function getEmployee(){
    
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = ()=>{
            
        if(xhr.readyState === 4 && xhr.status === 200){
             
            let data = JSON.parse(xhr.responseText);
            
            presentEmployee(data);
        }

    }
 
    xhr.open("GET", `employee.do`);

    xhr.send();
};

function presentEmployee(data){
    
    if(data.message){
        document.getElementById('employeeMessage').innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
    }
    else{
       
        let employee = document.getElementById("employee");

        let tableRowNode=document.createElement("tr")
        let tableDataNode1=document.createElement("td");
        let tableDataNode2=document.createElement("td");
        let tableDataNode3=document.createElement("td");

        let nameText  = document.createTextNode(`${data.lastName}, ${data.firstName}`);
        let usernameText  = document.createTextNode(`${data.username}`);
        let emailText     = document.createTextNode(`${data.email}`);
        
        tableDataNode1.appendChild(nameText);
        tableDataNode2.appendChild(usernameText);
        tableDataNode3.appendChild(emailText);

        tableRowNode.appendChild(tableDataNode1);
        tableRowNode.appendChild(tableDataNode2);
        tableRowNode.appendChild(tableDataNode3);

        employee.appendChild(tableRowNode);
        
    } 
}
