window.onload = () =>{

    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');
   
    //Get event listener
    document.getElementById("getPendReimbursement").addEventListener("click", getPendReimbursement);
    //Get all pending reimbursement as soon as the page loads

    //filter
   // document.getElementById("filter").addEventListener("keyup",filterTable);
    
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
    
        let counter = 0; 

        let reimbursementList = document.getElementById("pendReimbursement");
             reimbursementList.innerHTML="";
        
        data.forEach((reimbursement)=>{
        
        counter = counter + 1;            
        let tr = document.createElement('tr');   

        let td1 = document.createElement('td');
        let td2 = document.createElement('td');
        let td3 = document.createElement('td');
        let td4 = document.createElement('td');
        let td5 = document.createElement('td');
        let td6 = document.createElement('td');
         //td7 is reimbursement status
        let td7 = document.createElement('td');

        let selectList = document.createElement('select');
        selectList.className = 'selectpicker';  
        selectList.id = 'selectedStatus';   
        let option1 = document.createElement('option');
        let option2 = document.createElement('option');
        let option3 = document.createElement('option');

         //td8 is for action button
         let td8 = document.createElement('td');
         let button1 = document.createElement('button'); 
         let button2 = document.createElement('button'); 

         let text1 = document.createTextNode(`${reimbursement.id}`);
         let text2 = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
         console.log(reimbursement.requested);
         let text3 = document.createTextNode(`${reimbursement.amount}`);
         let text4 = document.createTextNode(`${reimbursement.description}`);
         let text5 = document.createTextNode(`${reimbursement.requester.firstName} ${reimbursement.requester.lastName}`);
         let text6 = document.createTextNode(`${reimbursement.type.type}`);
         //td 7 text
         let optionText1 = document.createTextNode(`${reimbursement.status.status}`);
         let optionText2 = document.createTextNode(`DECLINE`);
         let optionText3 = document.createTextNode(`APPROVE`); 

         //td8
        let textButton2 = document.createTextNode('UPDATE');
        button2.className = 'btn btn-sm btn-success';
        button2.setAttribute('onclick','finalizedReimbursement(this)');

            td1.appendChild(text1);
            td2.appendChild(text2);
            td3.appendChild(text3);
            td4.appendChild(text4);
            td5.appendChild(text5);
            td6.appendChild(text6);

            //td7
            option1.appendChild(optionText1);
            option2.appendChild(optionText2);
            option3.appendChild(optionText3);
            selectList.appendChild(option1);
            selectList.appendChild(option2);
            selectList.appendChild(option3);
            
            td7.appendChild(selectList);
            //td8
            button2.appendChild(textButton2);
            td8.appendChild(button2);

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tr.appendChild(td6);
          
            tr.appendChild(td7);
            tr.appendChild(td8);

            reimbursementList.appendChild(tr);
        });

    }
}



function finalizedReimbursement(obj){

   let rowData = obj.parentNode.parentNode;

 
   let statusCode =rowData.childNodes[7].childNodes[0].value ;
   let reimbursementId = rowData.childNodes[0].innerHTML;
   

   let statusID;
   if(statusCode === 'APPROVE'){
    statusID = 3;
   }
   else if(statusCode === 'DECLINE'){
    statusID = 2;
   }
   else{
    statusID = 1;
   }
   console.log(statusID);


   //AJAX Logic
   let xhr = new XMLHttpRequest();

   xhr.onreadystatechange = () => {
       if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
           //Getting JSON from response body
           let data = JSON.parse(xhr.responseText);
           console.log(data);   
           //Cal login response processing
           updateReimbursement(data);
       }
   };
     //Doing a HTTP to a specifc endpoint
    xhr.open("POST",`finalizeRequest.do?reimbursementId=${reimbursementId}&status=${statusCode}&statusId=${statusID}`);
//Sending our request
    xhr.send();

}

function updateReimbursement(data) {

    if(data.message === "A REIMBURSEMENT HAS BEEN UPDATED SUCCESSFULLY"){
      document.getElementById("listMessage").innerHTML = '<span class="label label-success label-center">Update successful.</span>';
      
      setTimeout(() =>{ window.location.replace("home.do");}, 3000);
 
    }
    else{
      document.getElementById("listMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
         
    }
 
}


function filterTable(){

    // Get variables 
  let filter = document.getElementById("filter").value.toUpperCase();
  let table = document.getElementById("pendReimbursement");
  let tr = table.getElementsByTagName("tr");
  let i, j;
  // Loop through all rows, hide those do not fit
  for (i = 0; i < tr.length; i++) {

    loop:  for(j = 0;j<6;j++){
        td = tr[i].getElementsByTagName("td")[j];
          if (td) {
             if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
                break loop;
           } else {
                tr[i].style.display = "none";
                
           }
        } 
        
      }
  
    }
}