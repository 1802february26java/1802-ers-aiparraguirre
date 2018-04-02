window.onload = () =>{

    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');
   
     getPendReimbursement();

}

function getPendReimbursement(){
      
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
          if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
        
            let data = JSON.parse(xhr.responseText);
            console.log(data);
 
            showPendReimbursement(data);
          }
      };
    xhr.open("GET",`multipleRequests.do?fetch=pending`);
 
    xhr.send();
}

function showPendReimbursement(data) {
     
    if(data.message){
        document.getElementById("listMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
    }else{
    
         

        let reimbursementList = document.getElementById("pendReimbursement");
             reimbursementList.innerHTML="";
        
        data.forEach((reimbursement)=>{
        
                   
        let tr = document.createElement('tr');   

        let td1 = document.createElement('td');
        let td2 = document.createElement('td');
        let td3 = document.createElement('td');
        let td4 = document.createElement('td');
        let td5 = document.createElement('td');
        let td6 = document.createElement('td');
        let td7 = document.createElement('td');
         
         
        let txt1 = document.createTextNode(`${reimbursement.id}`);
        let txt2 = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
        let txt3 = document.createTextNode(`${reimbursement.amount}`);
        let txt4 = document.createTextNode(`${reimbursement.description}`);
        let txt5 = document.createTextNode(`${reimbursement.requester.firstName} ${reimbursement.requester.lastName}`);
        let txt6 = document.createTextNode(`${reimbursement.type.type}`);
        let txt7 = document.createTextNode(`${reimbursement.status.status}`);
         
        td1.appendChild(txt1);
        td2.appendChild(txt2);
        td3.appendChild(txt3);
        td4.appendChild(txt4);
        td5.appendChild(txt5);
        td6.appendChild(txt6);
        td7.appendChild(txt7);


        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
        tr.appendChild(td6);
        tr.appendChild(td7);

        reimbursementList.appendChild(tr);
        
    });

    }
}



